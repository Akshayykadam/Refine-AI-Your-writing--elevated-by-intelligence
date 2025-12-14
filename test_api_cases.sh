#!/bin/bash

API_KEY="AIzaSyAQURefyOFS57RpjYLBeUWNQesRd_m0yDk"
MODEL="gemini-2.5-flash-lite"
URL="https://generativelanguage.googleapis.com/v1beta/models/$MODEL:generateContent?key=$API_KEY"

# System Prompt from GeminiClient.kt
SYSTEM_PROMPT="You are a helpful writing assistant. Your task is to rewrite the text provided by the user. Do not add any conversational filler. Return ONLY the rewritten text."

run_test() {
    TEST_NAME=$1
    INSTRUCTION=$2
    INPUT_TEXT=$3

    echo "---------------------------------------------------"
    echo "Running Test: $TEST_NAME"
    echo "Input: \"$INPUT_TEXT\""
    
    # Construct Full Prompt
    FULL_TEXT="$SYSTEM_PROMPT\n\nInstruction: $INSTRUCTION\nText to Rewrite: $INPUT_TEXT"
    
    # Escape for JSON
    SAFE_TEXT=$(echo "$FULL_TEXT" | sed 's/"/\\"/g' | tr '\n' '\\n')
    
    # Curl Request
    RESPONSE=$(curl -s -X POST "$URL" \
        -H "Content-Type: application/json" \
        -d "{ \"contents\": [{ \"parts\": [{ \"text\": \"$SAFE_TEXT\" }] }] }")

    # Extract Content (Simple grep/sed for quick check, or just print response if short)
    echo "Response:"
    echo "$RESPONSE" | grep -o '"text": "[^"]*"' | head -1
    
    # Check for Error
    if echo "$RESPONSE" | grep -q "error"; then
        echo "❌ FAILED"
        echo "$RESPONSE"
    else
        echo "✅ PASS"
    fi
    echo "---------------------------------------------------"
}

# Test Case 1: Grammar Check
GRAMMAR_PROMPT="Check the text for grammar, spelling, punctuation, and basic sentence structure errors. Correct only what is necessary to make the text grammatically correct and readable. Do not rewrite for style, tone, or clarity unless required for correctness. Do not change wording, intent, or sentence order beyond the minimum needed. Preserve the original tone and meaning exactly."
run_test "Grammar Check" "$GRAMMAR_PROMPT" "me fail english yes"

# Test Case 2: Warm Tone
WARM_PROMPT="Rewrite the text in a warm, supportive, and human tone. Sound approachable, respectful, and emotionally aware without being overly sentimental. Keep the message clear and sincere. Do not exaggerate emotions or add unnecessary affection. Preserve the original meaning."
run_test "Warm Tone" "$WARM_PROMPT" "stop spamming me right now"

# Test Case 3: Refine (Default)
REFINE_PROMPT="Rewrite the text to be clearer, more fluent, and easier to read while preserving the original meaning, intent, and length. Improve grammar, sentence flow, and word choice. Do not add new ideas, remove information, or change the tone. Keep it natural and neutral."
run_test "Refine Logic" "$REFINE_PROMPT" "i want to apply for this job plz hire me"
