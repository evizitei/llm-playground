.PHONY: test install run clean

test:
	uv run pytest tests/

install:
	uv sync

run:
	uv run python calculator.py

clean:
	find . -type d -name __pycache__ -exec rm -rf {} + 2>/dev/null || true
	find . -type f -name "*.pyc" -delete