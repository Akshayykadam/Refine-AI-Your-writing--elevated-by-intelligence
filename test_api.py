import requests
import json

API_KEY = "AIzaSyAQURefyOFS57RpjYLBeUWNQesRd_m0yDk"
MODEL = "gemini-2.5-flash-lite"
URL = f"https://generativelanguage.googleapis.com/v1beta/models/{MODEL}:generateContent?key={API_KEY}"

SYSTEM_PROMPT = "You are a helpful writing assistant. Your task is to rewrite the text provided by the user. Do not add any conversational filler. Return ONLY the rewritten text."

def run_test(name, instruction, input_text):
    print(f"--- Running Test: {name} ---")
    print(f"Input: {input_text}")
    
    full_prompt = f"{SYSTEM_PROMPT}\n\nInstruction: {instruction}\nText to Rewrite: {input_text}"
    
    payload = {
        "contents": [{
            "parts": [{"text": full_prompt}]
        }]
    }
    
    try:
        response = requests.post(URL, json=payload, headers={"Content-Type": "application/json"})
        response.raise_for_status()
        data = response.json()
        
        candidates = data.get("candidates", [])
        if candidates:
            text = candidates[0].get("content", {}).get("parts", [{}])[0].get("text", "").strip()
            print(f"✅ Result: {text}")
        else:
            print("❌ No candidates returned.")
            print(data)
            
    except Exception as e:
        print(f"❌ Failed: {e}")
        if 'response' in locals():
            print(response.text)
    print("\n")

# Tests
grammar_prompt = "Check the text for grammar, spelling, punctuation, and basic sentence structure errors. Correct only what is necessary to make the text grammatically correct and readable. Do not rewrite for style, tone, or clarity unless required for correctness. Do not change wording, intent, or sentence order beyond the minimum needed. Preserve the original tone and meaning exactly."
run_test("Grammar Check", grammar_prompt, "me fail english yes")

warm_prompt = "Rewrite the text in a warm, supportive, and human tone. Sound approachable, respectful, and emotionally aware without being overly sentimental. Keep the message clear and sincere. Do not exaggerate emotions or add unnecessary affection. Preserve the original meaning."
run_test("Warm Tone", warm_prompt, "stop spamming me right now")

refine_prompt = "Rewrite the text to be clearer, more fluent, and easier to read while preserving the original meaning, intent, and length. Improve grammar, sentence flow, and word choice. Do not add new ideas, remove information, or change the tone. Keep it natural and neutral."
run_test("Refine", refine_prompt, "i want to apply for this job plz hire me")
