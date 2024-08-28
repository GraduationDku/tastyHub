import React, { useState } from 'react';
import '../../css/Review/CreateReview.css';

function CreateRecipeReview({ recipeId }) {
  const [grade, setGrade] = useState(0);
  const [text, setText] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setIsSubmitting(true);
    setError(null);

    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/recipe-review/create/${recipeId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization' : localStorage.getItem('accessToken')
        },
        body: JSON.stringify({ grade, text }),
      });

      if (!response.ok) {
        throw new Error('리뷰 제출에 실패했습니다.');
      }

      // 성공 시, 리뷰 제출 후 초기화
      setGrade(0);
      setText('');
    } catch (error) {
      setError(error.message);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      
      <div className='createreview'>
        <div>
        <label htmlFor="grade"><strong>평점 </strong> </label>
        <br/><br/>
        <input
          type="number"
          id="grade"
          value={grade}
          onChange={(e) => setGrade(e.target.value)}
          min="0"
          max="5"
          required
        />
      </div>
      <br/>
      <div>
        <label htmlFor="text"><strong>리뷰 </strong></label>
        <br/><br/>
        <textarea
          id="text"
          value={text}
          onChange={(e) => setText(e.target.value)}
          required
        />
      </div>
      <br/><br/>
      <button type="submit" disabled={isSubmitting}>
        {isSubmitting ? '제출 중...' : '제출'}
      </button>
      {error && <p style={{ color: 'red' }}>{error}</p>}
    </div></form>
  );
}

export default CreateRecipeReview;
