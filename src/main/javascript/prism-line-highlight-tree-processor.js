function prismLineHighlightTreeProcessor () {
  this.process(function (doc) {
    const listings = doc.findBy({ context: 'listing' })
    for (let listing of listings) {
      const calloutLines = []
      const lines = listing.getSourceLines()
      for (let index = 0; index < lines.length; index++) {
        const line = lines[index];
        if (line.trim().match(/\/\/ <[0-9]+>$/)) {
          calloutLines.push(index + 1) // 0-based index
        }
      }
      listing.setAttribute('callout-lines', calloutLines.join(','))
    }
    return doc
  })
}

module.exports.register = function register (registry) {
  if (typeof registry.register === 'function') {
    registry.register(function () {
      this.treeProcessor(prismLineHighlightTreeProcessor)
    })
  } else if (typeof registry.block === 'function') {
    registry.treeProcessor(prismLineHighlightTreeProcessor)
  }
  return registry
}
