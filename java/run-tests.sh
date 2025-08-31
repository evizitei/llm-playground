#!/bin/bash

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Maven is not installed. Please install Maven first."
    echo "On macOS: brew install maven"
    echo "On Ubuntu/Debian: sudo apt-get install maven"
    exit 1
fi

# Clean and run tests
echo "Running JUnit tests for Calculator..."
mvn clean test

# Check exit code
if [ $? -eq 0 ]; then
    echo ""
    echo "✅ All tests passed successfully!"
else
    echo ""
    echo "❌ Some tests failed. Please check the output above."
    exit 1
fi