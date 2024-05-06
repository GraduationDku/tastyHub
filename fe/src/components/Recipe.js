import React, { useState, useEffect } from 'react';

function Recipe({ onRecipeSelect, setScreen, onEdit }) {
  const [recipes, setRecipes] = useState([]);

  useEffect(() => {
    async function fetchAllRecipes() {
      try {
        const response = await fetch('http://localhost:8080/recipe/list', {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json'
          }
        });
        const data = await response.json();
        if (Array.isArray(data.content)) {
          setRecipes(data.content); // 레시피 데이터를 상태에 저장
        } else {
          console.error('레시피 데이터 형식이 예상과 다릅니다:', data);
        }
      } catch (error) {
        console.error('레시피를 불러오는 중 오류 발생:', error);
      }
    }

    fetchAllRecipes();
  }, []);

  return (
    <div>
      <h1>전체 레시피 조회</h1>
      <button onClick={() => setScreen('create')}>레시피 작성하기</button>
      <ul>
        {recipes.map(recipe => (
          <li key={recipe.foodId} onClick={() => onRecipeSelect(recipe.foodId)}>
            <h3>{recipe.foodName}</h3>
            <img src={recipe.foodImgUrl} alt={recipe.foodName} style={{ width: '100px', height: '100px' }} />
            <div>
              {/* foodInformation 객체의 속성을 확인하여 데이터가 존재하는지 확인 */}
              <p>설명: {recipe.foodInformation ? recipe.foodInformation.text : '정보 없음'}</p>
              <p>요리 시간: {recipe.foodInformation ? recipe.foodInformation.cookingTime + '분' : '정보 없음'}</p>
              <p>인분: {recipe.foodInformation ? recipe.foodInformation.serving : '정보 없음'}</p>
            </div>
            <button onClick={(e) => {
              e.stopPropagation(); // 클릭 이벤트의 전파를 중지
              onEdit(recipe.foodId);
            }}>수정하기</button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default Recipe;
