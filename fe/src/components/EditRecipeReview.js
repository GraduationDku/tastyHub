import React, { useState, useEffect } from 'react';

function EditRecipeReview({ recipeReviewId }) {
  const [grade, setGrade] = useState(0);
  const [text, setText] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    async function fetchReviewDetails() {
      try {
        const response = await fetch(`http://localhost:8080/recipe-review/${recipeReviewId}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem('accessToken')
          }
        });
        if (response.ok) {
          const data = await response.json();
          setGrade(data.grade);
          setText(data.text);
        } else {
          throw new Error('Failed to fetch review details');
        }
      } catch (error) {
        console.error('Error fetching review details:', error);
        setError(error.message);
      }
    }

    fetchReviewDetails();
  }, [recipeReviewId]);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setIsSubmitting(true);
    setError(null);

    try {
      const response = await fetch(`http://localhost:8080/recipe-review/modify/${recipeReviewId}`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('accessToken')
        },
        body: JSON.stringify({ grade, text }),
      });

      if (!response.ok) {
        throw new Error('리뷰 수정에 실패했습니다.');
      }

      // 성공 시, 필요에 따라 후속 작업 수행 가능
    } catch (error) {
      setError(error.message);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <div>
        <label htmlFor="grade">평점:</label>
        <input
          type="number"
          id="grade"
          value={grade}
          onChange={(e) => setGrade(Number(e.target.value))}
          min="0"
          max="5"
          required
        />
      </div>
      <div>
        <label htmlFor="text">리뷰:</label>
        <textarea
          id="text"
          value={text}
          onChange={(e) => setText(e.target.value)}
          required
        />
      </div>
      <button type="submit" disabled={isSubmitting}>
        {isSubmitting ? '수정 중...' : '수정'}
      </button>
      {error && <p style={{ color: 'red' }}>{error}</p>}
    </form>
  );
}

export default EditRecipeReview;
