import { type ChangeEvent, useEffect, useState } from 'react'
import Button from './Button.tsx'

type PaginationProps = {
  page: number
  totalPages: number
  onPageChange: (page: number) => void
}

export default function Pagination({ page, totalPages, onPageChange }: PaginationProps) {
  const [inputValue, setInputValue] = useState(String(page + 1))

  useEffect(() => {
    setInputValue(String(page + 1))
  }, [page])

  const visiblePages =
    totalPages <= 3
      ? Array.from({ length: totalPages }, (_, index) => index)
      : page === 0
        ? [0, 1, 2]
        : page === totalPages - 1
          ? [totalPages - 3, totalPages - 2, totalPages - 1]
          : [page - 1, page, page + 1]

  function handleSubmit(event: ChangeEvent) {
    event.preventDefault()

    const nextPage = Number(inputValue) - 1

    if (Number.isNaN(nextPage) || nextPage < 0 || nextPage >= totalPages) {
      return
    }

    onPageChange(nextPage)
  }

  return (
    <div className="border-border bg-surface flex items-center justify-between gap-4 border-t px-4 py-3">
      <div className="flex gap-2">
        <Button
          label="<"
          variant="secondary"
          disabled={page === 0}
          onClick={() => onPageChange(page - 1)}
        />

        {visiblePages.map(pageNumber => (
          <Button
            key={pageNumber}
            label={String(pageNumber + 1)}
            variant={pageNumber === page ? 'primary' : 'secondary'}
            onClick={() => onPageChange(pageNumber)}
          />
        ))}

        <Button
          label=">"
          variant="secondary"
          disabled={page >= totalPages - 1}
          onClick={() => onPageChange(page + 1)}
        />
      </div>

      <form onSubmit={handleSubmit} className="flex items-center gap-2">
        <span className="text-muted text-sm">Page</span>
        <input
          value={inputValue}
          onChange={event => setInputValue(event.target.value)}
          onFocus={() => setInputValue('')}
          onBlur={() => {
            if (inputValue.trim() === '') {
              setInputValue(String(page + 1))
            }
          }}
          className="border-border bg-background text-dark focus:border-accent w-20 rounded-md border px-3 py-2 text-sm outline-none"
        />
        <span className="text-muted text-sm">of {totalPages}</span>
      </form>
    </div>
  )
}