import React, { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { fetchLikeCount, toggleLike } from '../../redux/Like/likeState';
import { FaThumbsUp, FaRegThumbsUp } from 'react-icons/fa'; // React Icons
import '../../css/RecipeDetail.css';

const LikeButton = ({ recipeId }) => {
  const dispatch = useDispatch();
  const { likeCount, liked, loading, error } = useSelector((state) => state.like);

  useEffect(() => {
    dispatch(fetchLikeCount(recipeId));
  }, [dispatch, recipeId]);

  const handleToggleLike = () => {
    dispatch(toggleLike(recipeId));
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;

  return (
    <div>
      <button
        className='recipedetail'
        onClick={handleToggleLike}
        style={{
          background: 'none',
          border: 'none',
          cursor: 'pointer',
          display: 'flex',
          alignItems: 'center',
          gap: '8px',
          color: liked ? '#3eab5c' : '#787878',
          fontSize: '16px',
          transition: 'transform 0.3s',
          marginLeft:'40px',
          marginRight:'-35px',

        }}
        onMouseOver={(e) => (e.currentTarget.style.transform = 'scale(1.1)')}
        onMouseOut={(e) => (e.currentTarget.style.transform = 'scale(1)')}
      >
        {liked ? <FaThumbsUp /> : <FaRegThumbsUp />}{likeCount}
      </button>
    </div>
  );
};

export default LikeButton;
