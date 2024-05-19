import React, {useState, useEffect} from 'react';

function EditPost ({postId}){
  const [formData, setFormData] = useState({
    postState: ''
  });

  useEffect(() => {
    async function fetchPost(){
      try {
        const response = await fetch(`http://localhost:8080/post/modify/${postId}`);
        const data = await response.json();
        setFormData({

        });
      }catch (error) {
        console.error('Error fetching recipe details', error)
      }
    }
    fetchPost();
  }, [postId]);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch(`http://localhost:8080/post/modify/${postId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData)
      });
      if (response.ok) {
        alert('Post updated successfully!');
      } else {
        throw new Error('Failed to update post');
      }
    } catch (error) {
      console.error('Error:', error);
      alert('Error updating post');
    }
  };

  //return ();
}
export default EditPost;