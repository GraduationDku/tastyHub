import React, { useState, useEffect } from 'react';
import '../css/MainScreen.css';

function MainScreen() {
  const [recipes, setRecipes] = useState([]);
  const [selectedRecipe, setSelectedRecipe] = useState(null);
  const [posts, setPosts] = useState([]);
  const [selectPost, setSelectedPost] = useState(null);
  const [page, setPage] = useState();
  const [size, setSize] = useState();
  const [sort, setSort] = useState('');

  useEffect(() => {
    async function fetchWeeklyRecipes() {
      try {
        const response = await fetch(`${process.env.REACT_APP_API_URL}/recipe/popular?page=${page}&size=${size}&sort=${sort}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          }
        });
        if (response.ok) {
          const data = await response.json();
          console.log('Received recipes data:', data);
          if (Array.isArray(data.content)) {
            setRecipes(data.content);
          } else {
            console.error('Data.content is not an array', data.content);
          }
        } else {
          throw new Error('Failed to fetch recipes');
        }
      } catch (error) {
        console.error('Error fetching weekly recipes:', error);
      }
    }

    async function fetchRecentPosts() {
      try {
        console.log('Fetching recent posts...');
        const response = await fetch(`${process.env.REACT_APP_API_URL}/post/recent/list?page=${page}&size=${size}&sort=${sort}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization' : localStorage.getItem('accessToken')
          }
        });
        console.log('Response status:', response.status);
        if (response.ok) {
          const data = await response.json();
          console.log('Received posts data:', data.content);
          if (Array.isArray(data.content)) {
            setPosts(data.content);
          } else {
            console.error('Data is not an array', data);
          }
        } else {
          throw new Error('Failed to fetch posts');
        }
      } catch (error) {
        console.error('Error fetching recent posts:', error);
      }
    }

    fetchWeeklyRecipes();
    fetchRecentPosts();
  }, [page, size, sort]); // 페이지, 사이즈, 정렬 값이 변경될 때마다 호출

  const handleRecipeClick = (recipe) => {
    setSelectedRecipe(recipe);
    console.log('Selected recipe:', recipe);
  };

  return (
    <body>
      <h1>주간 인기 레시피</h1>
      <div className='mainbox'>
        <ul>
          {recipes.map((recipe) => (
            <li key={recipe.foodId} onClick={() => handleRecipeClick(recipe)}>
              {recipe.title}  {recipe.foodName}
            </li>
          ))}
        </ul>
      
        {selectedRecipe && (
          <div>
            <h2>{selectedRecipe.title}</h2>
            <img src={selectedRecipe.foodImgUrl} alt={selectedRecipe.foodName} style={{ width: '300px' }} />
            {selectedRecipe.foodInformationDto ? (
              <>
                <p className='info'>요리 시간: {selectedRecipe.foodInformationDto.cookingTime || 'N/A'}분 | {selectedRecipe.foodInformationDto.serving || 'N/A'}</p>
                <p></p>
                <p>{selectedRecipe.foodInformationDto.content || 'No description available'}</p>
              </>
            ) : (
              <p>No additional food information available.</p>
            )}
          </div>
        )}
      </div>
      <h1>실시간 게시글</h1>
      <div className='posts'>
        
        <ul>
          {posts.map((post) => (
            <li key={post.postId}>
              <h3>{post.title}</h3>
              <p>{post.userName}</p>
              {/* <img src={post.userImg} alt={post.userName} style={{ width: '50px', height: '50px', borderRadius: '50%' }} /> */}
              <p>{post.postState}</p>
            </li>
          ))}
        </ul>
      </div>
    </body>
  );
}

export default MainScreen;
