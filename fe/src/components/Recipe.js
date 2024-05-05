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
        if (data.recipes) {
          setRecipes(data.recipes);  // 'recipes' 키로 레시피 데이터를 추출하여 상태에 저장
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
              <p>설명: {recipe.foodInformation.text}</p>
              <p>요리 시간: {recipe.foodInformation.cookingTime}분</p>
              <p>인분: {recipe.foodInformation.serving}</p>
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
