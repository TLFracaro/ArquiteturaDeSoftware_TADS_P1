/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Aluno;
import model.ProvaDao;

/**
 *
 * @author Tulio
 */
@WebServlet(name = "ControleAluno", urlPatterns = {"/ControleAluno"})
public class ControleAluno extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            ProvaDao dao = new ProvaDao();
            RequestDispatcher disp;
            String flag, mensagem;
            flag = request.getParameter("flag");
            if (flag.equals("cad_Aluno")) {
                Aluno aluno = new Aluno(
                        request.getParameter("matricula"),
                        request.getParameter("nome"),
                        request.getParameter("curso"),
                        request.getParameter("turno"),
                        Integer.parseInt(request.getParameter("duracao")),
                        Double.parseDouble(request.getParameter("valorCurso"))
                );

                switch (new ProvaDao().salvar(aluno)) {
                    case 1:
                        mensagem = "Aluno salvo com sucesso";
                        break;
                    case 2:
                        mensagem = "O aluno " + aluno.getNome() + " já está cadastrado";
                        break;
                    default:
                        mensagem = "Erro ao tentar salvar o aluno";
                        break;
                }
                request.setAttribute("mensagem", mensagem);
                request.getRequestDispatcher("mensagens.jsp").forward(request, response);
            } else if (flag.equals("list_Aluno")) {
                List<Aluno> alunos = new ProvaDao().listar("Aluno.findAll", Aluno.class);
                if (alunos == null) {
                    request.setAttribute("mensagem", "Não há alunos cadastrados");
                    request.getRequestDispatcher("mensagens.jsp").forward(request,
                            response);
                } else {
                    request.setAttribute("alunos", alunos);
                    request.getRequestDispatcher("listarAlunos.jsp").forward(request, response);
                }
            } else if (flag.equals("exc_Aluno")) {
                String matricula = request.getParameter("matricula");
                int retorno = new ProvaDao().excluir(matricula, Aluno.class);
                switch (retorno) {
                    case 1:
                        mensagem = "Aluno " + matricula + " Excluido com sucesso!";
                        break;
                    case 2:
                        mensagem = "Aluno " + matricula + " não cadastrado!";
                        break;
                    default:
                        mensagem = "Erro encontrado. Entre em contato com o suporte;";
                        break;
                }
                request.setAttribute("mensagem", mensagem);
                request.getRequestDispatcher("mensagens.jsp").forward(request,
                        response);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
