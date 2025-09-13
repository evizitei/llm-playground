"""Main module with example functionality."""

from typing import Union


def greet(name: str) -> str:
    """Return a greeting message.

    Args:
        name: The name to greet.

    Returns:
        A greeting message.
    """
    return f"Hello, {name}!"


def add(a: Union[int, float], b: Union[int, float]) -> Union[int, float]:
    """Add two numbers.

    Args:
        a: First number.
        b: Second number.

    Returns:
        Sum of a and b.
    """
    return a + b


def main() -> None:
    """Main entry point."""
    print(greet("World"))
    result = add(2, 3)
    print(f"2 + 3 = {result}")


if __name__ == "__main__":
    main()
