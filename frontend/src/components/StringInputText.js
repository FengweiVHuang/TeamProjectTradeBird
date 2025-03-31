function StringInputText({isPassword = false, label, onChange }) {
    return (
        <div>
            <label>{label}</label>
            <input type= {isPassword? "password":"text"} onChange={onChange} />
        </div>
    );
}

export default StringInputText;