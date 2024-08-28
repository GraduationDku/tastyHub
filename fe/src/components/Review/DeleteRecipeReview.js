import React, { useState } from 'react';

function DeleteRecipeReview({ recipeReviewId, onDeleteSuccess }) {
  const [isDeleting, setIsDeleting] = useState(false);
  const [error, setError] = useState(null);

  const handleDelete = async () => {
    setIsDeleting(true);
    setError(null);

    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/recipe-review/delete/${recipeReviewId}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('accessToken')
        }
      });

      if (!response.ok) {
        throw new Error('리뷰 삭제에 실패했습니다.');
      }

      // 삭제가 성공하면, 필요에 따라 후속 작업을 수행합니다.
      if (onDeleteSuccess) {
        onDeleteSuccess();
      }
    } catch (error) {
      setError(error.message);
    } finally {
      setIsDeleting(false);
    }
  };

  return (
    <div>
      <button onClick={handleDelete} disabled={isDeleting}>
        {isDeleting ? '삭제 중...' : '삭제'}
      </button>
      {error && <p style={{ color: 'red' }}>{error}</p>}
    </div>
  );
}

export default DeleteRecipeReview;
