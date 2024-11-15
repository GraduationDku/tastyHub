import os
import re
from typing import List

from pydantic import BaseModel
import openai

from dotenv import load_dotenv

load_dotenv()
API_KEY = os.getenv("API_KEY")
MODEL_NAME = os.getenv("MODEL_NAME")

class CookStep(BaseModel):
    stepNumber: int
    content: str

# 요청 본문 데이터 모델 정의
class RecipeRequest(BaseModel):
    foodName: str
    cookSteps: List[CookStep]
openai.api_key = API_KEY

def post_gpt(system_content, user_content):
    try:
        # 파인튜닝된 모델 호출
        response = openai.ChatCompletion.create(
            model="ft:gpt-4o-2024-08-06:personal:tastyhub:ARr0uzTC",
            messages=[
                {
                    "role": "system", "content": system_content
                },
                {
                    "role": "user", "content": user_content
                }
            ],
            temperature=0.9
        )
        answer = response.choices[0].message.content
        return answer
    except Exception as e:
        print(f"Error: {e}")
        return None

from typing import List
def create_prediction(foodName: str, steps: List[CookStep]):
    # 요리 단계 데이터 가공
    steps_content = "\n".join([step.content for step in steps])

    # 시스템 메시지와 사용자 요청 메시지 정의
    system_content = (
        "You are a culinary expert and a writer who translates cooking steps to English. "
        "Always use only the verbs that you were specifically trained on during fine-tuning, "
        "and ensure that all important details in each step are maintained. "
        "If a cooking step contains multiple verbs and nouns within a single sentence, "
        "keep only the portion up to the first verb and noun pair, and exclude the rest."
    )

    user_content = (
        "Translate the following cooking steps into English, ensuring each step follows a 'verb + noun' structure "
        "and that no details are omitted. Use only the verbs you were trained on, and avoid any additional or alternative verbs. "
        "Translate each step into a lowercase sentence. "
        f"The recipe name provided by the user is '{foodName}'. The cooking steps are:\n\n{steps_content}\n\n"
        "Ensure that each step captures all the necessary details provided, such as heating the pan, adding oil, etc. "
        "Translate and structure each step as a single sentence in English, ending with a period."
    )

    # GPT 요청 함수 호출
    ans = post_gpt(system_content, user_content)

    # None 처리 및 반환
    if ans:
        sentences = [sentence.strip() for sentence in ans.split('\n') if sentence.strip()]
    else:
        sentences = ["An error occurred and no response was received."]
    return sentences


