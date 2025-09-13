When creating a python project I prefer the following:

- uv as the virtual environment and dependency manager
- ruff as my linter
- pytest as the testing library, tests in a separate dir from src, no coverage tools.
- a Makefile for linting, testing, etc.
- no precommit hooks
- some standard development dependencies always installed: numpy, matplotlib, ipython



