import React, { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { fetchLikeCount, toggleLike } from '../../redux/likeState';
import '../../css/Like/Like.css';

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
      <button className='like' onClick={handleToggleLike}>
        {liked ? 'Unlike' : 'Like'} ({likeCount})
      </button>
    </div>
  );
};

export default LikeButton;
