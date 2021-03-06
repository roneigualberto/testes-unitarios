package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;

public class LocacaoServiceTest {

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	private LocacaoService service;

	private Usuario usuario;

	private List<Filme> filmes;

	@Before
	public void setUp() {
		service = new LocacaoService();
		usuario = new Usuario("Usuario 1");
		filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));
	}

	@Before
	public void tearDown() {

	}

	@Test
	public void testeLocacao() throws Exception {
		// acao
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verificacao
		assertThat(locacao.getValor(), is(equalTo(5.0)));
		assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));

	}

	@Test(expected = FilmeSemEstoqueException.class)
	public void testLocacao_filmeSemEstoque() throws Exception {

		// cenario
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));

		// acao
		service.alugarFilme(usuario, filmes);

	}

	@Test
	public void testLocacao_filmeSemEstoque_2() {

		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));
		// acao
		try {
			service.alugarFilme(usuario, filmes);
			fail("Deveria ter lançado a exceção");
		} catch (Exception e) {
			assertThat(e.getMessage(), is("Filme sem estoque"));
		}

	}

	@Test
	public void testLocacao_filmeSemEstoque_3() throws Exception {

		// cenario
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));
		expectedEx.expect(Exception.class);
		expectedEx.expectMessage("Filme sem estoque");

		// acao
		service.alugarFilme(usuario, filmes);
	}

	public void testLocacao_usuarioVazio() throws FilmeSemEstoqueException {

		// cenario
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));
		// acao
		try {
			service.alugarFilme(null, filmes);
			fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario vazio"));
		}

	}

	public void testLocacao_filmeVazio() throws FilmeSemEstoqueException, LocadoraException {

		// cenario

		expectedEx.expect(LocadoraException.class);
		expectedEx.expectMessage("Filme vazio");

		// acao
		service.alugarFilme(usuario, null);

	}

}
