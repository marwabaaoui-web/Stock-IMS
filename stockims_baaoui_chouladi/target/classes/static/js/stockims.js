document.addEventListener('DOMContentLoaded', () => {
  // 1) Filtre de recherche dans la liste des articles
  const champ = document.getElementById('recherche');
  if (champ) {
    const lignes = document.querySelectorAll('[data-ligne]');
    const compteur = document.getElementById('compteur');
    const aucun = document.getElementById('aucun-resultat');
    champ.addEventListener('input', () => {
      const q = champ.value.trim().toLowerCase();
      let visibles = 0;
      lignes.forEach(l => {
        const ok = l.textContent.toLowerCase().includes(q);
        l.hidden = !ok;
        if (ok) visibles++;
      });
      compteur.textContent = visibles;
      aucun.classList.toggle('d-none', visibles !== 0 || lignes.length === 0);
    });
  }

  // 2) Confirmation avant une sortie (le serveur valide dans tous les cas)
  document.querySelectorAll('form').forEach(f => {
    f.addEventListener('submit', e => {
      const bouton = e.submitter;
      if (bouton && bouton.dataset.confirm) {
        const q = f.querySelector('input[name="quantite"]');
        const message = bouton.dataset.confirm.replace('{q}', q && q.value ? q.value : '?');
        if (!window.confirm(message)) e.preventDefault();
      }
    });
  });

  // 3) Les messages de succès disparaissent automatiquement
  setTimeout(() => {
    document.querySelectorAll('.alert-success').forEach(a => bootstrap.Alert.getOrCreateInstance(a).close());
  }, 4000);

  // 4) Compteurs animés de la page d'accueil
  document.querySelectorAll('[data-count]').forEach(el => {
    const cible = parseInt(el.textContent, 10) || 0;
    const debut = performance.now();
    const duree = 900;
    const etape = t => {
      const p = Math.min((t - debut) / duree, 1);
      el.textContent = Math.round(cible * (1 - Math.pow(1 - p, 3)));
      if (p < 1) requestAnimationFrame(etape);
    };
    requestAnimationFrame(etape);
  });
});
