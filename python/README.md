# Python Project

A modern Python project with best practices and standard tooling.

## Features

- Modern Python packaging with `pyproject.toml`
- Code formatting with `ruff`
- Type checking with `mypy`
- Testing with `pytest`
- Pre-commit hooks for code quality
- Makefile for common tasks

## Installation

### For development

```bash
make dev
```

This will install all development dependencies and set up pre-commit hooks.

### For production use

```bash
make install
```

## Development

### Running tests

```bash
make test
```

### Linting

```bash
make lint
```

### Formatting code

```bash
make format
```

### Type checking

```bash
make typecheck
```

### Building distribution

```bash
make build
```

### Cleaning build artifacts

```bash
make clean
```

## Project Structure

```
.
├── src/              # Source code
│   ├── __init__.py
│   └── main.py       # Example module
├── tests/            # Test files
│   ├── __init__.py
│   └── test_main.py  # Example tests
├── pyproject.toml    # Project configuration
├── Makefile          # Common tasks
├── .gitignore        # Git ignore rules
├── .pre-commit-config.yaml  # Pre-commit hooks
└── README.md         # This file
```

## License

MIT