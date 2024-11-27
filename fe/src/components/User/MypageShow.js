import React, { useState } from 'react';
import '../../../src/css/MypageShow.css';

const MypageShow = ({ onRecipeSelect, userName }) => {
  const [view, setView] = useState(null);
  const [scrapedRecipes, setScrapedRecipes] = useState([]);
  const [writtenReviews, setWrittenReviews] = useState([]);
  const [myRecipes, setMyRecipes] = useState([]);
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(5);
  const [sort, setSort] = useState('createdAt');

  const fetchScrapedRecipes = async () => {
    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/scrap/list`, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('accessToken'),
        },
      });
      const data = await response.json();
      setScrapedRecipes(Array.isArray(data.content) ? data.content : []);
    } catch (error) {
      console.error('Failed to fetch scraped recipes:', error);
      setScrapedRecipes([]);
    }
  };

  const fetchWrittenReviews = async () => {
    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/recipe-review/my-list`, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('accessToken'),
        },
      });
      const data = await response.json();
      setWrittenReviews(data.content || []);
    } catch (error) {
      console.error('Failed to fetch written reviews:', error);
      setWrittenReviews([]);
    }
  };

  const fetchMyRecipes = async () => {
    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/recipe/mylist?page=${page}&size=${size}&sort=${sort}`, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('accessToken'),
        },
      });
      const data = await response.json();
      setMyRecipes(Array.isArray(data.content) ? data.content : []);
    } catch (error) {
      console.error('Failed to fetch my recipes:', error);
      setMyRecipes([]);
    }
  };

  return (
    <div className='mypageshow'>
      <h1>My Page</h1>
      <div className='box'>
        <button
          className='mypageshow'
          onClick={() => {
            setView('scraped');
            fetchScrapedRecipes();
          }}
        >
          스크랩한 레시피 모아보기
        </button>
        <br />
        <br />
        <button
          className='mypageshow'
          onClick={() => {
            setView('written');
            fetchWrittenReviews();
          }}
        >
          내가 작성한 레시피 리뷰 모아보기
        </button>
        <br />
        <br />
        <button
          className='mypageshow'
          onClick={() => {
            setView('myrecipes');
            fetchMyRecipes();
          }}
        >
          내가 작성한 레시피 모아보기
        </button>
        <br />
        <br />

        {view === 'scraped' && (
          <ScrapedRecipes recipes={scrapedRecipes} onRecipeSelect={onRecipeSelect} />
        )}
        {view === 'written' && (
          <WrittenReviews reviews={writtenReviews} onRecipeSelect={onRecipeSelect} />
        )}
        {view === 'myrecipes' && (
          <MyRecipes recipes={myRecipes} onRecipeSelect={onRecipeSelect} />
        )}
      </div>
    </div>
  );
};

const ScrapedRecipes = ({ recipes, onRecipeSelect }) => (
  <div className='show'>
    <h2 className='mypageshow'>레시피 스크랩</h2>
    <ul>
      {recipes.length > 0 ? (
        recipes.map((recipe) => (
          <li key={recipe.foodId} onClick={() => onRecipeSelect(recipe.foodId)}>
            <img src={recipe.foodImgUrl} alt={recipe.foodName} style={{ width: '240px' }} />
            <p>{recipe.foodName}</p>
          </li>
        ))
      ) : (
        <p>스크랩한 레시피가 없습니다.</p>
      )}
    </ul>
  </div>
);

const WrittenReviews = ({ reviews, onRecipeSelect }) => (
  <div className='mypageshow'>
    <h2 className='mypageshow'>작성한 레시피 리뷰</h2>
    <ul>
      {reviews.length > 0 ? (
        reviews.map((review) => (
          <li key={review.recipeId} onClick={() => onRecipeSelect(review.recipeId)}>
            <p>{review.foodName}</p>
            <p>평점: {review.grade}</p>
            <p>{review.content}</p>
          </li>
        ))
      ) : (
        <p>작성한 레시피 리뷰가 없습니다.</p>
      )}
    </ul>
  </div>
);

const MyRecipes = ({ recipes, onRecipeSelect }) => (
  <div className='show'>
    <h2 className='mypageshow'>내가 작성한 레시피</h2>
    <ul>
      {recipes.length > 0 ? (
        recipes.map((recipe) => (
          <li key={recipe.foodId} onClick={() => onRecipeSelect(recipe.foodId)}>
            <img src={recipe.foodImgUrl} alt={recipe.foodName} style={{ width: '240px' }} />
            <p>{recipe.foodName}</p>
          </li>
        ))
      ) : (
        <p>작성한 레시피가 없습니다.</p>
      )}
    </ul>
  </div>
);

export default MypageShow;
