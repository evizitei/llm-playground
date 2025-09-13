.PHONY: help install dev test lint format typecheck clean build

help:
	@echo "Available commands:"
	@echo "  make install    - Install production dependencies"
	@echo "  make dev        - Install development dependencies"
	@echo "  make test       - Run tests with coverage"
	@echo "  make lint       - Run ruff linter"
	@echo "  make format     - Format code with ruff"
	@echo "  make typecheck  - Run mypy type checking"
	@echo "  make clean      - Remove build artifacts and cache files"
	@echo "  make build      - Build distribution packages"

install:
	pip install -e .

dev:
	pip install -e ".[dev]"

test:
	pytest

lint:
	ruff check src tests

format:
	ruff format src tests
	ruff check --fix src tests

typecheck:
	mypy src

clean:
	rm -rf build dist *.egg-info
	rm -rf .pytest_cache .ruff_cache .mypy_cache
	rm -rf htmlcov .coverage
	find . -type d -name __pycache__ -exec rm -rf {} + 2>/dev/null || true
	find . -type f -name "*.pyc" -delete

build: clean
	python -m build