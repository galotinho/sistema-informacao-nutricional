$(document).ready(function () {
	moment.locale('pt-BR');
    var table = $('#table-ingrediente').DataTable({
    	language: {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Portuguese-Brasil.json"
        },
    	searching: true,
    	order: [[ 1, "asc" ]],
    	lengthMenu: [5, 10],
        processing: true,
        serverSide: true,
        responsive: true,
        ajax: {
            url: '/ingrediente/datatables/server',
            data: 'data'
        },
        columns: [
            {data: 'id'},
            {data: 'nome'},
            {orderable: false, data: 'carboidratos'
            },
            {orderable: false, data: 'proteinas'},
            {orderable: false, data: 'gordurastotais'},
            {orderable: false, data: 'gordurassaturadas'},
            {orderable: false, data: 'gordurastrans'},
            {orderable: false, data: 'fibraalimentar'},
            {orderable: false, data: 'sodio'},
            {orderable: false, 
             data: 'id',
                "render": function(id) {
                    return '<a class="btn btn-success btn-sm btn-block" href="/ingrediente/editar/'+ 
                    	id +'" role="button"><i class="fas fa-edit"></i></a>';
                }
            },
            {orderable: false,
             data: 'id',
                "render": function(id) {
                    return '<a class="btn btn-danger btn-sm btn-block" href="/ingrediente/excluir/'+ 
                    	id +'" role="button" data-toggle="modal" data-target="#confirm-modal"><i class="fas fa-times-circle"></i></a>';
                }               
            }
        ],
     });
});