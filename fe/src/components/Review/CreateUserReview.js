import React, { useState } from 'react';

function CreateUserReview({ userId }) {
  const [grade, setGrade] = useState(0);
  const [content, setContent] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setIsSubmitting(true);
    setError(null);

    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/user-review/create/${userName}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ grade, content }),
      });

      if (!response.ok) {
        throw new Error('리뷰 제출에 실패했습니다.');
      }

      // 성공 시, 리뷰 제출 후 초기화
      setGrade(0);
      setContent('');
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
          onChange={(e) => setGrade(e.target.value)}
          min="0"
          max="5"
          required
        />
      </div>
      <div>
        <label htmlFor="text">리뷰:</label>
        <textarea
          id="text"
          value={content}
          onChange={(e) => setContent(e.target.value)}
          required
        />
      </div>
      <button type="submit" disabled={isSubmitting}>
        {isSubmitting ? '제출 중...' : '제출'}
      </button>
      {error && <p style={{ color: 'red' }}>{error}</p>}
    </form>
  );
}

export default CreateUserReview;
