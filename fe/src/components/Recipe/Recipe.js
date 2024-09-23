import React, { useState, useEffect } from 'react';
import PageButton from '../../../src/components/PageButton';

function Recipe({ onRecipeSelect, setScreen, onEdit }) {
  const [recipes, setRecipes] = useState([]);
  const [page, setPage] = useState(0); // 현재 페이지
  const [size, setSize] = useState(5); // 페이지 당 아이템 수 기본값
  const [sort, setSort] = useState('date'); // 정렬 방식 기본값
  const [totalItems, setTotalItems] = useState(0); // 전체 게시글 수
  const [totalPages, setTotalPages] = useState(0); // 전체 페이지 수


  useEffect(() => {
    async function fetchAllRecipes() {
      try {
        const response = await fetch(`${process.env.REACT_APP_API_URL}/recipe/list?page=${page}&size=${size}&sort=${sort}`, {
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
          setTotalItems(data.content.length);
          setTotalPages(data.content.length / size);
        } else {
          console.error('Invalid data format:', data);
        }
      } catch (error) {
        console.error('Error fetching recipes:', error);
      }
    }

    fetchAllRecipes();
  }, [ page, size, sort ]);

  const handleSizeChange = (e) => {
    setSize(parseInt(e.target.value, 10) || 5);
    setPage(0); // 페이지를 1로 초기화
  };

  const handleSortChange = (e) => {
    setSort(e.target.value || 'date');
    setPage(0); // 페이지를 1로 초기화
  };

  const handlePageChange = (newPage) => {
    if (newPage < 0) newPage = 0; // 페이지 번호를 1보다 작지 않도록 설정
    if (newPage > totalPages) newPage = totalPages; // 페이지 번호를 전체 페이지 수보다 크지 않도록 설정
    setPage(newPage);
  };

  return (
    <div className='recipe'>
        <h1>전체 레시피 조회</h1>
        <button onClick={() => setScreen('create')}>레시피 작성하기</button>
        <br /><br />
        <div className='box'>
        
        <div>
        <br />
            <label>정렬 기준: </label>
            <select value={sort} onChange={handleSortChange}>
<<<<<<< HEAD
              <option value="create_at">날짜</option>
              <option value="food_name">제목</option>
              <option value="user_id">작성자</option>
=======
              <option value="createAt">날짜</option>
              <option value="foodName">제목</option>
              {/*<option value="nickname">작성자</option>*/}
>>>>>>> 60a1397adce662f338ecaf0c51e14aed585bf168
            </select>
        
            <label>게시글 수: </label>
            <select value={size} onChange={handleSizeChange}>
              <option value={5}>5개</option>
              <option value={10}>10개</option>
              <option value={20}>20개</option>
            </select>
          </div>
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
        </div><PageButton
            totalItems={totalItems} // 전체 게시글 수
            itemsPerPage={size} // 페이지당 게시글 수
            onPageChange={handlePageChange} // 페이지 변경 시 호출될 함수
          />
      </div>
    </div>
  );
}

export default Recipe;
