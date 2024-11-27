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
          요리 재료와 양을 표시하고, 아래 형식과 같이 요리 단계를 6개에서 10개 사이의 단계로 나누어 주세요. 각 요리 단계 내용은 자세하게 작성해주세요. 이 각 요리 단계에 맞는 유튜브 영상 시:분:초 양식의 타임라인을 포함한 JSON 형식으로 응답해 주세요. 요리가 여러 개 나오는 영상이면 대표 메뉴 하나만 응답해주세요. 최종 응답값, json filed name은 영어로 그대로 놔두고 json filed value만 한글로 번역해서 각 항목을 채워주세요.
        {
”foodName” :String,

”foodInformation” :
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
    "timeLine" :String
    “content” :String
   },
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


