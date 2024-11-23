import React, { useState, useEffect } from 'react';
import '../../css/Recipe.css';

function Recipe({ onRecipeSelect, setScreen, onEdit }) {
  const [recipes, setRecipes] = useState([]);
  const [page, setPage] = useState(0); // 현재 페이지
  const [size, setSize] = useState(5); // 페이지 당 아이템 수 기본값
  const [sort, setSort] = useState('createdAt'); // 정렬 방식 기본값
  const [totalItems, setTotalItems] = useState(0); // 전체 게시글 수

  useEffect(() => {
    async function fetchAllRecipes() {
      try {
        const response = await fetch(
          `${process.env.REACT_APP_API_URL}/recipe/list?page=${page}&size=${size}&sort=${sort}`,
          {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json',
              Authorization: localStorage.getItem('accessToken'),
            }
          }
        );

        if (response.ok) {
          const data = await response.json();
          if (Array.isArray(data.content)) {
            setRecipes(data.content);
            // totalItems 설정을 비워둡니다
            setTotalItems(0); // 강제로 0으로 설정
          } else {
            console.error('Invalid data format:', data);
          }
        } else {
          console.error('Error fetching recipes:', response.statusText);
        }
      } catch (error) {
        console.error('Error fetching recipes:', error);
      }
    }

    fetchAllRecipes();
  }, [page, size, sort]);

  const handleSizeChange = (e) => {
    setSize(parseInt(e.target.value, 10) || 5);
    setPage(0); // 페이지를 0으로 초기화
  };

  const handleSortChange = (e) => {
    setSort(e.target.value || 'createdAt');
    setPage(0); // 페이지를 0으로 초기화
  };

  const handlePageChange = (newPage) => {
    setPage(newPage); // 새로운 페이지 설정
  };

  const PageButton = ({ totalItems, itemsPerPage, currentPage, onPageChange }) => {
    const totalPages = Math.ceil(totalItems / itemsPerPage); // 전체 페이지 수 계산
    const pages = Array.from({ length: totalPages }, (_, i) => i); // 페이지 목록 생성

    return (
      <div>
        <button onClick={() => onPageChange(currentPage - 1)} disabled={currentPage === 0}>
          이전
        </button>
        {pages.map((pageNum) => (
          <button
            key={pageNum}
            onClick={() => onPageChange(pageNum)}
            disabled={currentPage === pageNum}
          >
            {pageNum + 1} {/* 1부터 시작하도록 페이지 표시 */}
          </button>
        ))}
        <button onClick={() => onPageChange(currentPage + 1)} disabled={currentPage === totalPages - 1}>
          다음
        </button>
      </div>
    );
  };

  return (
    <body>
      <h1>전체 레시피 조회</h1>
      <button onClick={() => setScreen('create')}>레시피 작성하기</button>
      <br /><br />
      <div className="recipebox">
        <div className="select-container">
          <br />
          <select value={sort} onChange={handleSortChange}>
            <option value="createdAt">날짜</option>
            <option value="title">제목</option>
          </select>
          <select value={size} onChange={handleSizeChange}>
            <option value={5}>5개</option>
            <option value={10}>10개</option>
            <option value={20}>20개</option>
          </select>
        </div>
        <br />
        <br />
        <div className="seperate">
          <ul>
            {recipes.map((recipe) => (
              <li key={recipe.foodId} onClick={() => onRecipeSelect(recipe.foodId)}>
                <h3>{recipe.foodName}</h3>
                <img
                  src={recipe.foodImgUrl}
                  alt={recipe.foodName}
                  style={{ width: '50%' }}
                />
                <div>
                </div>
              </li>
            ))}
          </ul>
        </div>
        <PageButton
          totalItems={totalItems}
          itemsPerPage={size}
          currentPage={page}
          onPageChange={handlePageChange}
        />
      </div>
    </body>
  );
}

export default Recipe;
