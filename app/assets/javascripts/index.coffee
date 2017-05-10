$ ->
  $.get "/employees", (employees) ->
    $.each employees, (index, employee) ->
      $("#employees").append $("<li>").text employee.name