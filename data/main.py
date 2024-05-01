'''
환경 : 쥬피터 노트북
0. https://pypi.org/project/googletrans/
번역 api - 각 단계 진행 시 해당 string 번역하여 값 반환
하이퍼링크 제거 메서드 생성

1. 테이블 정의 - 레시피, 요리정보, 재료, 요리단계

레시피 - Recipe
1. id - db 자동배정
2. foodName : title
3. foodImg : image
4. tempId : spoonacular api recipeId

요리정보 - FoodInformation
1. id - db 자동 배정
2. text - summary
3. cookingTime - readyInMinutes
4. serving - serving

재료 - Ingredients 항목에서 가져오기
1. id - db 자동배정
2. ingredientName : ingredientName
3. amount : amount

요리 단계 - CookStep : analyzedInstruction 항목에서 가져오기
1. id - db 자동
2. stepNumber - number
3. text - step
4. stepImg - null로 통일

2. docker - mysql
docker volume 설정으로 레시피 및 db 저장 폴더 지정
파이썬 mysql 연결 flask x

3. 레시피 아이디 들고와서 레시피 객체 생성 후 db 저장
4. 레시피 아이디를 리스트로 받아오기
5. 받아온 아이디를 기반으로 단일 레시피 get 요청 진행
    5-1. 요리 정보 : 데이터 분리, body에서 summary 추출,이미지 링크 삭제, 그리고 매핑 및 객체 생성,
    5-2. 재료 : 테이블과 각 값 매핑해서 객체 생성,
    5-3. 요리 단계 : 테이블과 각 값 매핑해서 객체 생성
    각 순서마다 db에 상황마다 바로 전송

'''

def print_hi(name):
    # Use a breakpoint in the code line below to debug your script.
    print(f'Hi, {name}')  # Press ⌘F8 to toggle the breakpoint.


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    print_hi('PyCharm')

# See PyCharm help at https://www.jetbrains.com/help/pycharm/

