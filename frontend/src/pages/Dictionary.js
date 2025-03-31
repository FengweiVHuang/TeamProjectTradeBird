import AxiosRequests from "../util/AxiosRequests";
import IntStringButtonHorizontal from "../components/IntStringButtonHorizontal";
import axiosApi from "../util/AxiosRequests";

function Dictionary() {
    const handleAdd = async (pair) => {
        axiosApi.post('/dictionary/add', {
                id: pair.int,
                word: pair.string
            }
        ).then(response => {
            alert('Word added successfully');
            console.log(response);
        }).catch(error => {
            alert('You do not have permission to add words, you need to be an admin or a developer');
            console.error(error);
        });
    }


    return (
        <div>
            <IntStringButtonHorizontal onSubmit={handleAdd} intText="ID" stringText="Word" buttonText="Add Word"/>
        </div>
    );
}

export default Dictionary;