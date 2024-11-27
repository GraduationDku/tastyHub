import React, { useState } from 'react';
import PageButton from '../../../src/components/PageButton';
import '../../../src/css/MypageShow.css';

const MypageShow = ({ userName }) => {
  const [view, setView] = useState(null);
  const [scrapedRecipes, setScrapedRecipes] = useState([]);
  const [receivedReviews, setReceivedReviews] = useState([]);
  const [writtenReviews, setWrittenReviews] = useState([]);
  const [myRecipes, setMyRecipes] = useState([]);
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(5);
  const [sort, setSort] = useState('createdAt');
  const [totalItems, setTotalItems] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const handleSizeChange = (e) => {
    setSize(parseInt(e.target.value, 10) || 5);
    setPage(1);
  };

  const handleSortChange = (e) => {
    setSort(e.target.value || 'date');
    setPage(1);
  };

  const handlePageChange = (newPage) => {
    if (newPage < 1) newPage = 1;
    if (newPage > totalPages) newPage = totalPages;
    setPage(newPage);
  };

  const fetchScrapedRecipes = async () => {
    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/scrap/list`, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('accessToken')
        },
      });
      const data = await response.json();
      setScrapedRecipes(Array.isArray(data.content) ? data.content : []);
      setTotalItems(data.totalItems); 
      setTotalPages(Math.ceil(data.totalItems / size)); 
    } catch (error) {
      console.error('Failed to fetch scraped recipes:', error);
      setScrapedRecipes([]);
    }
  };

  const fetchWrittenReviews = async () => {
    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/recipe-review/mylist?page=${page}&size=${size}&sort=${sort}`, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('accessToken')
        },
      });
      const data = await response.json();
      setWrittenReviews(Array.isArray(data.content) ? data.content : []);
      setTotalItems(data.totalItems);
      setTotalPages(Math.ceil(data.totalItems / size));
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
          'Authorization': localStorage.getItem('accessToken')
        },
      });
      const data = await response.json();
      setMyRecipes(Array.isArray(data.content) ? data.content : []);
      setTotalItems(data.totalItems);
      setTotalPages(Math.ceil(data.totalItems / size));
    } catch (error) {
      console.error('Failed to fetch my recipes:', error);
      setMyRecipes([]);
    }
  };

  return (
    <div className='mypageshow'>
        <h1>My Page</h1><div className='box'>
        <button className='mypageshow' onClick={() => { setView('scraped'); fetchScrapedRecipes(); }}>스크랩한 레시피 모아보기</button>
        <br/>
        <button className='mypageshow' onClick={() => { setView('written'); fetchWrittenReviews(); }}>내가 작성한 레시피 리뷰 모아보기</button>
        <br/>
        <button className='mypageshow' onClick={() => { setView('myrecipes'); fetchMyRecipes(); }}>내가 작성한 레시피 모아보기</button>
        <br/>

        {view && (
          <div className='searchsort'>
            <select value={sort} onChange={handleSortChange} >
              <option value="createdAt">날짜</option>
              <option value="title">제목</option>
              <option value="nickname">작성자</option>
            </select>


            <select value={size} onChange={handleSizeChange}>
              <option value={5}>5개</option>
              <option value={10}>10개</option>
              <option value={20}>20개</option>
            </select>
          </div>
        )}

        {view === 'scraped' && <ScrapedRecipes recipes={scrapedRecipes} onPageChange={handlePageChange} />}
        {view === 'written' && <WrittenReviews reviews={writtenReviews} onPageChange={handlePageChange} />}
        {view === 'myrecipes' && <MyRecipes recipes={myRecipes} onPageChange={handlePageChange} />}
      </div>
    </div>
  );
};

const ScrapedRecipes = ({ recipes, onPageChange }) => (
  <div className='show'>
    <br/><br/><br/>
    <h2  className='mypageshow'>레시피 스크랩</h2>
    <ul>
      {recipes.length > 0 ? (
        recipes.map(recipe => (
          <li key={recipe.foodId}>
            <img src={recipe.foodImgUrl} alt={recipe.foodName} style={{width:"240px"}} />
            <p>{recipe.foodName}</p>
          </li>
        ))
      ) : (
        <p>스크랩한 레시피가 없습니다.</p>
      )}
    </ul>

  </div>
);

const WrittenReviews = ({ reviews, onPageChange }) => (
  <div className='mypageshow'>
    <br/><br/><br/>
    <h2  className='mypageshow'>작성한 레시피 리뷰</h2>
    <ul>
      {reviews.length > 0 ? (
        reviews.map(review => (
          <li key={review.recipeId}>
            <img src={review.foodImgUrl} alt={review.foodname} />
            <p>{review.foodname}</p>
            <p>평점 : {review.grade}</p>
            <p>{review.content}</p>
          </li>
        ))
      ) : (
        <p>작성한 레시피 리뷰가 없습니다.</p>
      )}
    </ul>

  </div>
);

const MyRecipes = ({ recipes, onPageChange }) => (
  <div className='show'>
    <br/><br/><br/>
    <h2  className='mypageshow'>내가 작성한 레시피</h2>
    <ul>
      {recipes.length > 0 ? (
        recipes.map(recipe => (
          <li key={recipe.foodId}>
            <img src={recipe.foodImgUrl} alt={recipe.foodName}
            style={{width:"240px"}}/>
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
