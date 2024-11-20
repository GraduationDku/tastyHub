import React, { useState, useEffect } from 'react';
import { FaBookmark, FaRegBookmark } from 'react-icons/fa'; // React Icons
import '../../css/RecipeDetail.css';

const ScrapButton = ({ recipeId }) => {
  const [isScrapped, setIsScrapped] = useState(false);

  useEffect(() => {
    // Check local storage for scrap status when the component mounts
    const savedScrapStatus = localStorage.getItem(`scrapStatus-${recipeId}`);
    if (savedScrapStatus) {
      setIsScrapped(JSON.parse(savedScrapStatus));
    }
  }, [recipeId]);

  const toggleScrap = async () => {
    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/scrap/${recipeId}`, {
        method: 'POST',
        headers: {
          Authorization: localStorage.getItem('accessToken'),
          'Content-Type': 'application/json',
        },
      });

      if (response.ok) {
        const newScrapStatus = !isScrapped;
        setIsScrapped(newScrapStatus);

        // Save the new scrap status to local storage
        localStorage.setItem(`scrapStatus-${recipeId}`, JSON.stringify(newScrapStatus));
      } else {
        console.error('Error toggling scrap status:', response.statusText);
      }
    } catch (error) {
      console.error('Error toggling scrap status:', error);
    }
  };

  return (
    <div>
      <button
        className='scrap'
        onClick={toggleScrap}
        style={{
          background: 'none',
          border: 'none',
          cursor: 'pointer',
          display: 'flex',
          alignItems: 'center',
          gap: '8px',
          color: isScrapped ? '#3eab5c' : '#787878',
          fontSize: '16px',
          transition: 'transform 0.3s',
        }}
        onMouseOver={(e) => (e.currentTarget.style.transform = 'scale(1.1)')}
        onMouseOut={(e) => (e.currentTarget.style.transform = 'scale(1)')}
      >
        {isScrapped ? <FaBookmark /> : <FaRegBookmark />} {isScrapped ? 'Scrapped' : 'Scrap'}
      </button>
    </div>
  );
};

export default ScrapButton;
