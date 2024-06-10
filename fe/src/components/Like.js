import React, { useState, useEffect } from 'react';

const LikeButton = ({ recipeId, token }) => {
  const [likeCount, setLikeCount] = useState(0);
  const [liked, setLiked] = useState(false);

  // 좋아요 개수를 가져오는 함수
  const fetchLikeCount = async () => {
    try {
      const response = await fetch(`/like/count/${recipeId}`);
      const data = await response.json();
      setLikeCount(data.likeCnt);
      setLiked(data.liked);  
    } catch (error) {
      console.error('Error fetching like count:', error);
    }
  };

  // 좋아요 상태를 변경하는 함수
  const toggleLike = async () => {
    try {
      const response = await fetch(`/like/${recipeId}`, {
        method: 'POST',
        headers: {
          'Authorization': localStorage.getItem('accessToken'),
          'Content-Type': 'application/json'
        }
      });

      if (response.ok) {
        fetchLikeCount();  // 서버에서 최신 좋아요 상태를 받아옴
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
    <div>
      <button onClick={toggleLike}>
        {liked ? 'Unlike' : 'Like'} ({likeCount})
      </button>
    </div>
  );
};

export default LikeButton;
