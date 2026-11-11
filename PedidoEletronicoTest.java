import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class PedidoEletronicoTest {

    private PedidoEletronico pedido;

    @BeforeEach
    public void setUp() throws Exception {
        pedido = new PedidoEletronico(1, "Cliente Teste", 150);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Teste finalizado.");
    }

    @Test
    public void testCalcularValorFinalDesconto() {
        assertEquals(135.0, pedido.calcularValorFinal(), 0.01);
    }

    @Test
    public void testCalcularFrete() {
        assertEquals(20 + 0.10 * 150, pedido.calcularFrete(), 0.01);
    }

    @Test
    public void testProcessarPedidoValido() {
        assertDoesNotThrow(() -> pedido.processarPedido());
        assertEquals(StatusPedido.PROCESSANDO, pedido.getStatus());
    }

    @Test
    public void testProcessarPedidoCancelado() {
        pedido.setStatus(StatusPedido.CANCELADO);
        PedidoInvalidoException e = assertThrows(PedidoInvalidoException.class, () -> pedido.processarPedido());
        assertEquals("Não é possível processar pedido cancelado.", e.getMessage());
    }

    @Test
    public void testAlterarStatusEntregue() throws PedidoInvalidoException {
        pedido.setStatus(StatusPedido.ENTREGUE);
        PedidoInvalidoException e = assertThrows(PedidoInvalidoException.class, () -> pedido.setStatus(StatusPedido.PENDENTE));
        assertEquals("Não pode mudar status de pedido entregue.", e.getMessage());
    }

    @Test
    public void testValorInvalido() {
        ValorInvalidoException ex = assertThrows(ValorInvalidoException.class, () -> {
            new PedidoEletronico(2, "Cliente", 0);
        });
        assertEquals("Valor do pedido deve ser maior que zero.", ex.getMessage());
    }
}
