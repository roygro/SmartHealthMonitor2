package mx.edu.utng.tv

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import kotlinx.coroutines.launch

class MainFragment : BrowseSupportFragment() {

    private lateinit var viewModel: TvViewModel
    private lateinit var histAdapter: ArrayObjectAdapter
    private lateinit var estadoAdapter: ArrayObjectAdapter
    private lateinit var alertasAdapter: ArrayObjectAdapter
    private lateinit var rowsAdapter: ArrayObjectAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar ViewModel manualmente
        viewModel = ViewModelProvider(requireActivity()).get(TvViewModel::class.java)

        // Configuración del BrowseFragment
        title = "SmartHealth TV"
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true

        // Color de la marca en el sidebar
        brandColor = resources.getColor(R.color.sh_primary, null)

        cargarFilas()
        observarDatos()
    }

    private fun cargarFilas() {
        rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

        // ── Fila 1: Estado actual (FC + Pasos) ───────────
        estadoAdapter = ArrayObjectAdapter(FCCardPresenter())
        estadoAdapter.add(LecturaFC(id = 0, valorBpm = 88, hora = "Ahora"))
        estadoAdapter.add(LecturaFC(id = 1, valorBpm = 4250, hora = "Pasos"))
        rowsAdapter.add(ListRow(HeaderItem("Estado actual"), estadoAdapter))

        // ── Fila 2: Historial de FC ────────────────────
        histAdapter = ArrayObjectAdapter(FCCardPresenter())
        rowsAdapter.add(ListRow(HeaderItem("Historial FC"), histAdapter))

        // ── Fila 3: Alertas recientes ────────────────────
        alertasAdapter = ArrayObjectAdapter(FCCardPresenter())
        rowsAdapter.add(ListRow(HeaderItem("Alertas recientes"), alertasAdapter))

        this.adapter = rowsAdapter
    }

    private fun observarDatos() {
        // Observar historial y actualizar la fila
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.historial.collect { lecturas ->
                    histAdapter.clear()
                    // Mostrar últimas 10 lecturas
                    val ultimasLecturas = lecturas.takeLast(10)
                    ultimasLecturas.forEach { histAdapter.add(it) }

                    // Actualizar alertas (solo las que no son normales)
                    alertasAdapter.clear()
                    val alertas = lecturas.filter { !it.esNormal }.takeLast(5)
                    alertas.forEach { alertasAdapter.add(it) }

                    // Actualizar estado actual con la última lectura
                    if (lecturas.isNotEmpty()) {
                        val ultima = lecturas.last()
                        estadoAdapter.clear()
                        estadoAdapter.add(
                            LecturaFC(
                                id = 0,
                                valorBpm = ultima.valorBpm,
                                hora = ultima.hora,
                                esNormal = ultima.esNormal
                            )
                        )
                        // Mantener el item de pasos
                        estadoAdapter.add(LecturaFC(id = 1, valorBpm = 4250, hora = "Pasos"))
                    }
                }
            }
        }
    }
}