import os
import json
import hashlib
import sys
import argparse
import numpy as np
import dotenv
import voyageai
import anthropic

def load_docs():
    filenames = ["java/README.md", "node/README.md", "ruby/README.md", "./README.md"]
    readme_texts = {filename: open(filename).read() for filename in filenames}
    return readme_texts

def get_docs_hash(readme_texts):
    """Generate a hash of the document contents to check if embeddings are still valid."""
    content_hash = hashlib.md5()
    for filename in sorted(readme_texts.keys()):
        content_hash.update(filename.encode())
        content_hash.update(readme_texts[filename].encode())
    return content_hash.hexdigest()

def save_embeddings_to_disk(embeddings, docs_hash):
    """Save embeddings and document hash to disk."""
    cache_data = {
        "docs_hash": docs_hash,
        "embeddings": embeddings
    }
    with open("embeddings_cache.json", "w") as f:
        json.dump(cache_data, f)
    print("Embeddings saved to disk")

def load_embeddings_from_disk():
    """Load embeddings from disk if they exist."""
    if os.path.exists("embeddings_cache.json"):
        with open("embeddings_cache.json", "r") as f:
            cache_data = json.load(f)
        print("Embeddings loaded from disk")
        return cache_data
    return None

def embed_docs(readme_texts):
    """Generate embeddings for documents, either from cache or Voyage API."""
    docs_hash = get_docs_hash(readme_texts)
    
    cache_data = load_embeddings_from_disk()
    if cache_data and cache_data.get("docs_hash") == docs_hash:
        print("Using cached embeddings")
        return cache_data["embeddings"]
    print("Generating new embeddings via Voyage API")
    voyage_client = voyageai.Client(api_key=os.getenv("VOYAGE_API_KEY"))
    documents = list(readme_texts.values())
    embeddings = {}
    embedded_responses = voyage_client.embed(documents, model="voyage-3.5-lite", input_type="document")
    doc_keys = list(readme_texts.keys())
    
    for i, response in enumerate(embedded_responses.embeddings):
        embeddings[doc_keys[i]] = response
        print(doc_keys[i])
        print(response)
        print("-" * 100)
    
    save_embeddings_to_disk(embeddings, docs_hash)
    return embeddings

def embed_query(query_text):
    voyage_client = voyageai.Client(api_key=os.getenv("VOYAGE_API_KEY"))
    response = voyage_client.embed([query_text], model="voyage-3.5-lite", input_type="query")
    return response.embeddings[0]

def cosine_similarity(vec1, vec2):
    vec1 = np.array(vec1)
    vec2 = np.array(vec2)
    dot_product = np.dot(vec1, vec2)
    norm1 = np.linalg.norm(vec1)
    norm2 = np.linalg.norm(vec2)
    return dot_product / (norm1 * norm2)

def rank_documents_by_similarity(query_embedding, doc_embeddings):
    similarities = []
    for doc_name, doc_embedding in doc_embeddings.items():
        similarity = cosine_similarity(query_embedding, doc_embedding)
        similarities.append((doc_name, similarity))
    similarities.sort(key=lambda x: x[1], reverse=True)
    return similarities

def similarity_mode(query):
    """Run similarity mode: rank documents by relevance to query."""
    print(f"Searching for: {query}")

    readme_texts = load_docs()
    doc_embeddings = embed_docs(readme_texts)

    query_embedding = embed_query(query)
    ranked_docs = rank_documents_by_similarity(query_embedding, doc_embeddings)

    print("\nDocuments ranked by relevance:")
    for i, (doc_name, similarity) in enumerate(ranked_docs, 1):
        print(f"{i}. {doc_name} (similarity: {similarity:.4f})")

    return ranked_docs

def rag_mode(query):
    """Run RAG mode: find most relevant document and use it as context for Claude API."""
    print(f"RAG query: {query}")

    readme_texts = load_docs()
    doc_embeddings = embed_docs(readme_texts)

    query_embedding = embed_query(query)
    ranked_docs = rank_documents_by_similarity(query_embedding, doc_embeddings)

    top_doc_name, top_similarity = ranked_docs[0]
    top_doc_content = readme_texts[top_doc_name]

    print(f"\nUsing most relevant document: {top_doc_name} (similarity: {top_similarity:.4f})")

    prompt = f"""Based on the following documentation, please answer the user's question.

Documentation from {top_doc_name}:
{top_doc_content}

User question: {query}

Please provide a helpful answer based on the documentation above."""

    client = anthropic.Anthropic(api_key=os.getenv("CLAUDE_API_KEY"))

    try:
        message = client.messages.create(
            model="claude-3-7-sonnet-20250219",
            max_tokens=1000,
            messages=[
                {"role": "user", "content": prompt}
            ]
        )

        print(f"\nClaude's response:")
        print(message.content[0].text)

    except Exception as e:
        print(f"Error calling Claude API: {e}")
        print("Make sure you have set the CLAUDE_API_KEY environment variable.")




if __name__ == "__main__":
    dotenv.load_dotenv()

    parser = argparse.ArgumentParser(description="RAG system for searching and querying documents")
    parser.add_argument("mode", choices=["similarity", "rag"],
                       help="Mode: 'similarity' to rank documents by relevance, 'rag' to get AI answer with context")
    parser.add_argument("query", help="Query string to search for")

    # Handle backward compatibility with old usage
    if len(sys.argv) == 2 and sys.argv[1] not in ["similarity", "rag"]:
        # Old usage: python script.py "query"
        print("Legacy mode detected. Running similarity search...")
        similarity_mode(sys.argv[1])
    elif len(sys.argv) == 1:
        # No arguments - just generate embeddings
        readme_texts = load_docs()
        embeddings = embed_docs(readme_texts)
        print(f"Loaded embeddings for {len(embeddings)} documents")
    else:
        # New argparse usage
        args = parser.parse_args()

        if args.mode == "similarity":
            similarity_mode(args.query)
        elif args.mode == "rag":
            rag_mode(args.query)
    