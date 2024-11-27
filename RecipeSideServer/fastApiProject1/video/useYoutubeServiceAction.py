###
### Sample code to demonstrate how to query a YouTube video, taking into account only the audio but not the video of it.
###
import json

import videoinsights_client
from .examples.common.api_key_settings import get_api_key
from videoinsights_client.models import YTQuery
from videoinsights_client import ApiException

import re


def youtubeAnalysis(video_url):
  # YouTube URL에서 video ID 추출
  match = re.search(r"(?:v=|\/)([0-9A-Za-z_-]{11})", video_url)
  if not match:
    print("유효하지 않은 YouTube URL입니다.")
    return
  video_id = match.group(1)

  # API 키 및 설정 준비
  api_key_dict = {'VideoInsightsAuthentication': get_api_key()}
  configuration = videoinsights_client.Configuration(api_key=api_key_dict)

  with videoinsights_client.ApiClient(configuration) as api_client:
    # API 클래스 인스턴스 생성
    api_instance = videoinsights_client.QueryApi(api_client)

    try:
      print("YouTube Query 엔드포인트 호출 중")

      # 쿼리 생성
      # 쿼리 생성
      q = YTQuery(
        query="""
              요리 영상을 분석하여 아래 형식에 따라 JSON 응답을 생성해 주세요. 

      1. **대표 메뉴 이름 (`foodName`)**: 요리 영상에서 만들어지는 대표적인 요리의 이름을 추출하세요. 
      2. **요리 정보 (`foodInformation`)**: 대표 메뉴에 대한 간단한 설명을 작성하세요.
      3. **재료 정보 (`ingredients`)**: 요리에서 사용된 재료와 각각의 양을 추출하세요.
      4. **요리 단계 (`cookSteps`)**:
         - 요리 과정을 6개에서 10개의 단계로 나누어 주세요.
         - 각 단계는 순서 번호(`stepNumber`), 타임라인(`timeLine`), 그리고 자세한 설명(`content`)을 포함합니다.
         - 타임라인은 해당 요리 단계가 시작하는 시각을 소수점 없이 초 단위로 **정확히** 제공해 주세요.
      5. **특별 지시사항**: 
         - 영상에서 여러 요리가 등장하면, 대표 메뉴 하나만 선택해 주세요.
         - JSON 구조는 아래 형식을 유지하며, `field name`은 영어로 그대로 두고 `field value`는 한국어로 번역해 주세요.

      요구하는 JSON 형식:
      ```json
      {
        "foodName": "String",
        "foodInformation": {
          "content": "String"
        },
        "ingredients": [
          {
            "ingredientName": "String",
            "amount": "String"
          }
        ],
        "cookSteps": [
          {
            "stepNumber": Long,
            "timeLine": "String",
            "content": "String"
          }
        ]
      }
          """,
          video_id=video_id
      )

      # API에 요청을 보내고 응답을 캡처
      api_response = api_instance.yt_query_v1(q)

      print("쿼리에 대한 응답:\n")
      print(api_response.response)
      response = api_response.response

      # 앞부분과 끝부분의 'json"""' 제거
      cleaned_response = response.strip().replace('```json', '',
                                                  1).replace('```',
                                                             '',
                                                             1).strip()

      # 응답이 문자열로 도착했을 경우, 이를 JSON 객체로 변환
      json_data = json.loads(cleaned_response)
      return json_data


    except ApiException as e:
      print(f"YouTube Query 엔드포인트 호출 중 예외 발생: {e}")


