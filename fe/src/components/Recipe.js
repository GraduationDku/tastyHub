import React, { useState, useEffect } from 'react';
import '../css/Recipe.css';

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
          setRecipes(data.content);
        } else {
          console.error('Invalid data format:', data);
        }
      } catch (error) {
        console.error('Error fetching recipes:', error);
      }
    }

    fetchAllRecipes();
  }, []);

  return (
    <div className='recipe'>
      <div className='box'>
        <h1>전체 레시피 조회</h1>
        <button onClick={() => setScreen('create')}>레시피 작성하기</button>
        <ul>
          {recipes.map(recipe => (
            <li key={recipe.foodId} onClick={() => onRecipeSelect(recipe.foodId)}>
              <h3>{recipe.foodName}</h3>
              <img src={recipe.foodImgUrl} alt={recipe.foodName} style={{ width: '100px', height: '100px' }} />
              <div>
                <p>설명: {recipe.foodInformationDto ? recipe.foodInformationDto.text : '정보 없음'}</p>
                <p>요리 시간: {recipe.foodInformationDto ? recipe.foodInformationDto.cookingTime + '분' : '정보 없음'}</p>
                <p>인분: {recipe.foodInformationDto ? recipe.foodInformationDto.serving : '정보 없음'}</p>
              </div>
              <button onClick={(e) => {
                e.stopPropagation();
                console.log('Edit button clicked for:', recipe.foodId);
                onEdit(recipe.foodId);
                }}>수정하기</button>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
}

export default Recipe;
