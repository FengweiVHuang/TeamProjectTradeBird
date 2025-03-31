import {useState} from "react";

const IntStringButtonHorizontal = ({onSubmit, intText, stringText, buttonText}) => {
    const [int, setInt] = useState('');
    const [string, setString] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit({int, string});
    };

    return (
        <form onSubmit={handleSubmit}>
            <div>
                <label>{intText}</label>
                <input
                    type="number"
                    value={int}
                    onChange={(e) => setInt(e.target.value)}
                    required
                />
            </div>
            <div>
                <label>{stringText}</label>
                <input
                    type="text"
                    value={string}
                    onChange={(e) => setString(e.target.value)}
                    required
                />
            </div>
            <button type="submit">{buttonText}</button>
        </form>
    );
}

export default IntStringButtonHorizontal;