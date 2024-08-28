import React, { useState, useEffect } from 'react';

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
          'Authorization': localStorage.getItem('accessToken'),
          'Content-Type': 'application/json'
        }
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
        onClick={toggleScrap}
        style={{
          backgroundColor: isScrapped ? 'green' : 'gray',
          color: 'white',
          padding: '10px',
          border: 'none',
          borderRadius: '10px',
          cursor: 'pointer',
          width : '80px',
          height : '50px'
          
        }}
      >
        {isScrapped ? 'Scrapped' : 'Scrap'}
      </button>
    </div>
  );
};

export default ScrapButton;
