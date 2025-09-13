"""Tests for the main module."""


from src.main import add, greet


class TestGreet:
    """Test cases for the greet function."""

    def test_greet_with_name(self) -> None:
        """Test greeting with a name."""
        assert greet("Alice") == "Hello, Alice!"

    def test_greet_with_empty_string(self) -> None:
        """Test greeting with empty string."""
        assert greet("") == "Hello, !"


class TestAdd:
    """Test cases for the add function."""

    def test_add_integers(self) -> None:
        """Test adding two integers."""
        assert add(2, 3) == 5

    def test_add_floats(self) -> None:
        """Test adding two floats."""
        assert add(2.5, 3.7) == 6.2

    def test_add_mixed(self) -> None:
        """Test adding integer and float."""
        assert add(2, 3.5) == 5.5

    def test_add_negative(self) -> None:
        """Test adding negative numbers."""
        assert add(-2, -3) == -5

    def test_add_zero(self) -> None:
        """Test adding with zero."""
        assert add(5, 0) == 5
        assert add(0, 0) == 0
