logs=$(cat test-logs.txt)
pattern="Tests run: ([0-9]+), Failures: ([0-9]+), Errors: ([0-9]+), Skipped: ([0-9]+)(?!.*, Time elapsed:.*)"
found_error=false
i=0

echo "123"
echo "$logs"
echo "456"

echo "$logs" | while IFS= read -r log; do
    if echo "$log" | grep -qE "$pattern"; then
        tests_run=$(echo "$log" | grep -oE "Tests run: ([0-9]+)" | cut -d' ' -f3)
        failures=$(echo "$log" | grep -oE "Failures: ([0-9]+)" | cut -d' ' -f2)
        errors=$(echo "$log" | grep -oE "Errors: ([0-9]+)" | cut -d' ' -f2)
        skipped=$(echo "$log" | grep -oE "Skipped: ([0-9]+)" | cut -d' ' -f2)
        echo "Tests run: $tests_run, Failures: $failures, Errors: $errors, Skipped: $skipped"
        i=$((i + 1))
        if [ "$failures" -gt 0 ] || [ "$errors" -gt 0 ]; then
            echo $( [ "$i" -eq 0 ] && echo "Unit tests failed" || echo "Integration tests failed" )
            found_error=true
        fi
    fi
done
if [ "$found_error" = true ]; then
    exit 1
fi