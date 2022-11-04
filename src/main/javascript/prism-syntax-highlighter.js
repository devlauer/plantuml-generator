const PrismExtension = {
  format (node, lang) {
    const nowrap = !(node.getDocument().getAttribute('prewrap')) || (node.isOption('nowrap'))
    const dataLine = node.getAttribute('callout-lines') ? ` data-line="${node.getAttribute('callout-lines')}"` : ''
    const preClass = ` class="${nowrap ? 'nowrap' : ''}"${dataLine}`
    return `<pre${preClass}><code class="language-${lang}" data-lang=${lang}>${node.getContent()}</code></pre>`
  },

  handlesHighlighting () {
    return false
  },

  hasDocinfo() {
    return false
  }
}

module.exports = PrismExtension
module.exports.register = function register (registry) {
  Opal.Asciidoctor.SyntaxHighlighter.register('prism', PrismExtension)
}
