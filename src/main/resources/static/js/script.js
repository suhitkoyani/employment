const search = () => {
    let query = $("#search-input").val();
    if (query == "") {
        $(".search-result").hide();
      } else {
        let url = `http://localhost:8081/search/${query}`;
        fetch(url)
            .then((response) => {
            return response.json();
            })
            .then((data) => {
                let text = `<div class='list-group'>`;
                data.forEach((employee) => {
                    text += `<a href='/show-employee/${employee.empId}' class='list-group-item list-group-item-action'> ${employee.empName} </a>`;
                });
                text += `</div>`;
                $(".search-result").html(text);
                $(".search-result").show();
            });

        
    }
}

