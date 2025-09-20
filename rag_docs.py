import os
import json
import hashlib
import dotenv
import voyageai

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
    
    # Save to cache
    save_embeddings_to_disk(embeddings, docs_hash)
    return embeddings

    


if __name__ == "__main__":
    dotenv.load_dotenv()
    readme_texts = load_docs()
    embeddings = embed_docs(readme_texts)
    print(f"Loaded embeddings for {len(embeddings)} documents")