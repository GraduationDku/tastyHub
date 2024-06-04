import React, { useState } from "react";

function CreatePost() {
  const [formData, setFormData] = useState({
    title: '',
    text: ''
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch('http://localhost:8080/post/create', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('accessToken')
        },
        body: JSON.stringify({
          text: formData.text,
          title: formData.title
        })
      });
      if (response.ok) {
        alert('Post created successfully!');
      } else {
        throw new Error('Failed to create post');
      }
    } catch (error) {
      console.error('Error:', error);
      alert('Error creating post');
    }
  };

  return (
    <>
        <form onSubmit={handleSubmit}>
        제목 :
        <input
          type="text"
          name="title"
          value={formData.title}
          onChange={handleChange}
        />
        내용 :
        <input
          type="text"
          name="text"
          value={formData.text}
          onChange={handleChange}
        />
        <button type="submit">작성하기</button>
      </form>
    </>
  );
}

export default CreatePost;
