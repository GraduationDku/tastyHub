import React, { useState, useEffect } from 'react';
import '../css/MypageShow.css';

const MypageShow = ({userId}) => {
  const [view, setView] = useState(null);
  const [scrapedRecipes, setScrapedRecipes] = useState([]);
  const [receivedReviews, setReceivedReviews] = useState([]);
  const [writtenReviews, setWrittenReviews] = useState([]);
  const [myRecipes, setMyRecipes] = useState([]);

  const fetchScrapedRecipes = async () => {
    try {
      const response = await fetch('http://localhost:8080/scrap/list', {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('accessToken')
        },
      });
      const data = await response.json();
      console.log(data)
      setScrapedRecipes(Array.isArray(data) ? data : []);
    } catch (error) {
      console.error('Failed to fetch scraped recipes:', error);
      setScrapedRecipes([]);
    }
  };

  const fetchReceivedReviews = async () => {
    try {
      const response = await fetch(`http://localhost:8080/user-review/list/${userId}`, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('accessToken')
        },
      });
      console.log(response)
      const data = await response.json();
      console.log(data)
      
      setReceivedReviews(Array.isArray(data) ? data : []);
    } catch (error) {
      console.error('Failed to fetch received reviews:', error);
      setReceivedReviews([]);
    }
  };

  const fetchWrittenReviews = async () => {
    try {
      const response = await fetch('http://localhost:8080/recipe-review/list', {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('accessToken')
        },
      });
      const data = await response.json();
      console.log(data)
      setWrittenReviews(Array.isArray(data) ? data : []);
    } catch (error) {
      console.error('Failed to fetch written reviews:', error);
      setWrittenReviews([]);
    }
  };

  const fetchMyRecipes = async () => {
    try {
      const response = await fetch('http://localhost:8080/recipe/mylist', {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('accessToken')
        },
      });
      const data = await response.json();
      console.log(data.content)
      setMyRecipes(Array.isArray(data.content) ? data.content: []);
    } catch (error) {
      console.error('Failed to fetch my recipes:', error);
      setMyRecipes([]);
    }
  };

  return (
    <div className='mypageshow'>
      <div className='box'>
      <h1>My Page</h1>
      <br></br><br></br><br></br>
      <button onClick={() => { setView('scraped'); fetchScrapedRecipes(); }}>스크랩한 레시피 모아보기</button>
      <br/>
      <br/>
      <button onClick={() => { setView('received'); fetchReceivedReviews(); }}>내가 받은 리뷰 모아보기</button>
      <br/>
      <br/>
      <button onClick={() => { setView('written'); fetchWrittenReviews(); }}>내가 작성한 레시피 리뷰 모아보기</button>
      <br/>
      <br/>
      <button onClick={() => { setView('myrecipes'); fetchMyRecipes(); }}>내가 작성한 레시피 모아보기</button>
      <br/>
      <br/>
      
      {view === 'scraped' && <ScrapedRecipes recipes={scrapedRecipes} />}
      {view === 'received' && <ReceivedReviews reviews={receivedReviews} />}
      {view === 'written' && <WrittenReviews reviews={writtenReviews} />}
      {view === 'myrecipes' && <MyRecipes recipes={myRecipes} />}
    </div></div>
  );
};

const ScrapedRecipes = ({ recipes }) => (
  <div className='show'>
    <h2>레시피 스크랩</h2>
    <ul>
      {recipes.length > 0 ? (
        recipes.map(recipe => (
          <li key={recipe.foodId}>
            <img src={recipe.foodImgUrl} alt={recipe.foodName} />
            <p>{recipe.foodName}</p>
          </li>
        ))
      ) : (
        <p>No scraped recipes available.</p>
      )}
    </ul>
  </div>
);

const ReceivedReviews = ({ reviews }) => (
  <div className='show'>
    <h2>받은 리뷰</h2>
    <ul>
      {reviews.length > 0 ? (
        reviews.map(review => (
          <li key={review.userId}>
            <p>{review.username}</p>
            <p>Grade: {review.grade}</p>
            <p>{review.text}</p>
          </li>
        ))
      ) : (
        <p>No reviews received.</p>
      )}
    </ul>
  </div>
);

const WrittenReviews = ({ reviews }) => (
  <div className='show'>
    <h2>작성한 레시피 리뷰</h2>
    <ul>
      {reviews.length > 0 ? (
        reviews.map(review => (
          <li key={review.recipeId}>
            <img src={review.foodImgUrl} alt={review.foodname} />
            <p>{review.foodname}</p>
            <p>평점 : {review.grade}</p>
            <p>{review.text}</p>
          </li>
        ))
      ) : (
        <p>No written reviews available.</p>
      )}
    </ul>
  </div>
);

const MyRecipes = ({ recipes }) => (
  <div className='show'>
    <h2>내가 작성한 레시피</h2>
    <ul>
      {recipes.length > 0 ? (
        recipes.map(recipe => (
          <li key={recipe.foodId}>
            <img src={recipe.foodImgUrl} alt={recipe.foodName} />
            <p>{recipe.foodName}</p>
            <p>{recipe.foodInformationDto.text}</p>
            <p>조리 시간 : {recipe.foodInformationDto.cookingTime} minutes</p>
            <p>양 : {recipe.foodInformationDto.serving}</p>
          </li>
        ))
      ) : (
        <p>No recipes available.</p>
      )}
    </ul>
  </div>
);

export default MypageShow;
