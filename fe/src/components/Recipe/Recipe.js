import React, { useState, useEffect } from 'react';
import '../../css/Recipe/Recipe.css';

function Recipe({ onRecipeSelect, setScreen, onEdit }) {
  const [recipes, setRecipes] = useState([]);

  useEffect(() => {
    async function fetchAllRecipes() {
      try {
        const response = await fetch(`${process.env.REACT_APP_API_URL}/recipe/list`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization' : localStorage.getItem('accessToken')
          }
        });
        if(response.ok){
        }
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
        <h1>전체 레시피 조회</h1>
        <div className='box'>
        <button onClick={() => setScreen('create')}>레시피 작성하기</button>
        <div className='seperate'>
        <ul>
          {recipes.map(recipe => (
            <li key={recipe.foodId} onClick={() => onRecipeSelect(recipe.foodId)}>
              <h3>{recipe.foodName}</h3>
              <img src={recipe.foodImgUrl} alt={recipe.foodName} style={{ width: '50%'}} />
              <div>
                <p>요리 시간: {recipe.foodInformationDto ? recipe.foodInformationDto.cookingTime + '분' : '정보 없음'} | {recipe.foodInformationDto ? recipe.foodInformationDto.serving : '정보 없음'}인분</p>
                <p>설명: {recipe.foodInformationDto ? recipe.foodInformationDto.content : '정보 없음'}</p>
                
              </div>
              
            </li>
          ))}
        </ul>
        </div>
      </div>
    </div>
  );
}

export default Recipe;
