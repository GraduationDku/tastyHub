import React, { useState, useEffect } from 'react';
import '../../css/Like.css'

const LikeButton = ({ recipeId }) => {
  const [likeCount, setLikeCount] = useState(0);
  const [liked, setLiked] = useState(false);

  // Fetch the like count and status
  const fetchLikeCount = async () => {
    try {
      const response = await fetch(`http://localhost:8080/like/count/${recipeId}`, {
        headers: {
          'Authorization': localStorage.getItem('accessToken')
        }
      });
      
      if (response.ok) {
        const data = await response.json();
        setLikeCount(data.count);
        setLiked(data.liked);
        console.log(data)
      } else {
        console.error('Failed to fetch like count');
      }
    } catch (error) {
      console.error('Error fetching like count:', error);
    }
  };

  // Toggle like status
  const toggleLike = async () => {
    try {
      const response = await fetch(`http://localhost:8080/like/${recipeId}`, {
        method: 'POST',
        headers: {
          'Authorization': localStorage.getItem('accessToken'),
          'Content-Type': 'application/json'
        }
      });
      if (response.ok) {
        fetchLikeCount(); // Refresh like count and status
      } else {
        console.error('Error toggling like status:', response.statusText);
      }
    } catch (error) {
      console.error('Error toggling like status:', error);
    }
  };

  useEffect(() => {
    fetchLikeCount();
  }, [recipeId]);

  return (
    <div >
      <button className='like' onClick={toggleLike}>
        {liked ? 'Unlike' : 'Like'} ({likeCount})
      </button>
    </div>
  );
};

export default LikeButton;
