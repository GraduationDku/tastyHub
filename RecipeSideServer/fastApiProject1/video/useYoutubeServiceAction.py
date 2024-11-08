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
      q = YTQuery(
          query="""
        요리 재료와 양을 표시하고, 아래 형식으로 요리 단계를 JSON 형식으로 응답해 주세요.
        {
”foodName” :String,

”foodInformaion” :
   {
     “content” :String
   }
”ingredients” : [
   {
     “ingredientName” :String
     “amount” :String
   },
]
”cookSteps” : [
   {
    “stepNumber” :Long
    “content” :String
   },
]
}
        각 단계에 맞는 유튜브 영상 타임라인도 포함해주세요.
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
