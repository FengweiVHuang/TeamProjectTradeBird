// `frontend/src/components/AuthForm.js`
import React, { useState } from 'react';
import "../pages/css/Login.css"
import logo from "../Resource/logo.png";

const AuthForm = ({ onSubmit, buttonText }) => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit({ username, password });
    };

    return (
        <form onSubmit={handleSubmit} className={"auth-form"}>
            <div className="logo-container">
                <img src={logo} alt="Logo"/>
            </div>
            <div>
                <label>Username:</label>
                <input
                    type="text"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required
                />
            </div>
            <div>
                <label>Password:</label>
                <input
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
            </div>

            <a href="/v2/resetpassword" style={{width:"100%" ,textAlign:"right",marginBottom:"10px"}}>Forget Password</a><a
            href="/v2/register" style={{width: "100%", textAlign: "right", marginBottom: "10px"}}>Register</a>
            <button type="submit">{buttonText}</button>
        </form>
    );
};

export default AuthForm;