import './css/NewPost.css';
import React, {useState} from 'react';
import axiosApi from "../util/AxiosRequests";
import {useNavigate} from "react-router-dom";

function NewPost({tag}) {

    const [previewImage, setPreviewImage] = useState(null);
    const [file, setFile] = useState(null);
    const [selectedTags, setSelectedTags] = useState([]);


    const handleImageChange = (event) => {
        const file = event.target.files[0];
        if (file) {
            const imageUrl = URL.createObjectURL(file);
            if(!file.type.startsWith('image/'))
            {
                alert("Please upload an image file");
                setFile (null);
                return;
            }
            const img = new Image();
            img.src = imageUrl;

            img.onload = function() {
                if(img.width > 3000 || img.height > 3000)
                {
                    alert("Image resolution must be smaller than 3000x3000");
                    setFile (null);
                }
                else {
                    setFile(file);
                    setPreviewImage(imageUrl);
                }
            }
        }
        else
        {
            alert("Please upload an image file");
        }
    };

    const handleCheckboxChange = (tag) => {
        setSelectedTags((prevSelectedTags) =>
            prevSelectedTags.includes(tag)
                ? prevSelectedTags.filter((t) => t !== tag)
                : [...prevSelectedTags, tag]
        );
    };
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();

        const formData = new FormData();
        const itemName = event.target.itemName.value;
        const price = event.target.price.value;
        const description = event.target.description.value;

        if (!price || isNaN(price) || price <= 0 || price > 100000|| price*1000%10!==0) {
            alert('please input a valid price');
            return;
        }
        if(file === null)
        {
            alert("Please upload an image file");
            return;
        }
        if (selectedTags.length === 0) {
            alert('please select at least one tag');
            return;
        }
        if (file.size > 4 * 1024 * 1024) {
            alert('File size must be smaller than 4MB');
        }
        if (file.width > 3000 || file.height > 3000) {
            alert('Image resolution must be smaller than 3000x3000');
        }

        formData.append('file', file);
        formData.append('itemName', itemName);
        formData.append('price', Math.floor(price * 100) / 100);
        formData.append('description', description);
        formData.append('tags', JSON.stringify(selectedTags));

        try {
            const response = await axiosApi.post('posts/create', formData);
            console.log(response);
            alert('Post successful');
            navigate('/v2/detail');
        } catch (error) {
            console.error(error);
            alert('Post failed');
        }
    };

    return (
        <div className="input-area topNewPostPage scrollbar-vo scrollbar " >
            <h2 >New Post</h2>
            <form id="newPostForm" onSubmit={handleSubmit} >
                <div className="input-area" >
                    <p className="title">Item Name:</p>
                    <input
                        id="itemName"
                        name="itemName"
                        className="input-place"
                        maxLength={255}
                        placeholder="Input your goods name. Max 255 words."
                        required
                    />
                </div>
                <div className="input-area">
                    <p className="title">Price:</p>
                    <input
                        id="price"
                        name="price"
                        type="number"
                        step="0.01"
                        className="input-place"
                        required
                        min={0}
                        placeholder="Input your expected price."
                    />
                </div>

                <div className="input-area">
                    <p className="title">Tag :</p>
                    <div className="input-place scroll-ho scrollbar scrollbar-ho">
                        {Object.keys(tag).map((key) => (
                            <div key={tag[key]} className="checkbox-item">
                                <input
                                    type="checkbox"
                                    id={`tag-${tag[key]}`}
                                    value={key}
                                    checked={selectedTags.includes(tag[key])}
                                    onChange={() => handleCheckboxChange(tag[key])}
                                />
                                <label htmlFor={`tag-${tag[key]}`}>{key}</label>
                            </div>
                        ))}
                    </div>
                </div>

                <div className="input-area">
                <p className="title">Introduction:</p>
                    <textarea
                        id="description"
                        name="description"
                        className="input-place"
                        rows="10"
                        cols="50"
                        required
                        placeholder="Brief introduce your goods. May contain your contact information, expected payment method, and other detail information."
                    ></textarea>
                </div>
                <div className="input-area">
                    <p className="title">Picture:</p>
                    <input
                        id="pic"
                        name="file"
                        className="input-place"
                        type="file"
                        accept="image/jpeg, image/png"
                        required
                        onChange={handleImageChange}
                    />
                </div>
                <div>
                    <img
                        src={previewImage}
                        alt="preview"
                        style={{
                            height: '230px',
                            width: 'auto',
                            maxWidth: '70%',
                            border: '1px solid #ccc',
                            display: previewImage ? 'block' : 'none',
                            margin: '0 30%',
                        }}
                    />
                </div>

                <button type="submit" style={{ border: 0, width: '100%',margin:"10px 0px" }}>
                    Submit
                </button>
            </form>
        </div>
    );
}



export default NewPost;

