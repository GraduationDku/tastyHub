import React, { useState, useEffect } from 'react';

function EditPost({ postId }) {
    const [formData, setFormData] = useState({
        postState: '공유 전' // 초기 상태는 '공유 전'
    });

    useEffect(() => {
        async function fetchPost() {
            try {
                const response = await fetch(`http://localhost:8080/post/modify/${postId}`);
                const data = await response.json();
                setFormData({
                    postState: data.postState || '공유 전'
                });
            } catch (error) {
                console.error('Error fetching post details', error);
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

    const handleStatusChange = () => {
        const nextStatus = {
            '공유 전': '공유 중',
            '공유 중': '공유 완료',
            '공유 완료': '공유 전'
        };
        setFormData({
            ...formData,
            postState: nextStatus[formData.postState]
        });
    };

    return (
        <form onSubmit={handleSubmit}>
            <h1>Edit Post</h1>
            <div>
                <label>Post State: {formData.postState}</label>
                <button type="button" onClick={handleStatusChange}>
                    {formData.postState}
                </button>
            </div>
            <button type="submit">Save Changes</button>
        </form>
    );
}

export default EditPost;
