import os
import re

import openai

from dotenv import load_dotenv

load_dotenv()
API_KEY = os.getenv("API_KEY")
MODEL_NAME = os.getenv("MODEL_NAME")


def post_gpt(system_content, user_content):
    try:
        openai.api_key = API_KEY
        response = openai.Completion.create(
            model=MODEL_NAME,
            messages=[
                {
                    "role": "system", "content": system_content
                },
                {
                    "role": "user", "content": user_content
                }
            ],
            stop=None,
            temperature=0.9
        )
        answer = response.choices[0].message.content
        return answer
    except Exception as e:
        return None


def create_prediction(foodName, steps):
    system_content = "You are a culinary expert and a writer who edits. Unify the styles posted by the user according to the set tone and style."
    user_content = "한글로 답변해줘; 사용자가 입력한 요리제목은" + f' {foodName}이다.; 사용자가 입력한 '+f'{steps}가 해당 요리에 적합한지 확인을 해줘; 해당 요리 단계는 한 줄씩 정리해서 출력을 해야하며 문장의 끝맺음은 "-이다"로 진행해줘; 그리고 이때 사용자의 요리 과정에 다른 추가 과정을, 개선점을 입력하지마. 오로지 사용자의 요리 단계가 해당 요리이름에 적합한지, 그리고 요리과정의 문체만 손봐서 올려줘 \n\n'
    ans = post_gpt(system_content, user_content)
    sentences = [ans.strip() for sentence in ans if sentence.strip()]
    return sentences

