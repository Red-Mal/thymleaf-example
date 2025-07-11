# SVG Icon Guide

## Common Issues & Solutions

### 1. Icon Not Visible
**Problem:** SVG icon doesn't show up
**Solution:** 
- Use `class="w-6 h-6"` instead of `class="size-6"`
- Ensure parent has text color (e.g., `text-white`)
- Use `stroke="currentColor"` for stroke-based icons

### 2. Button Not Visible (Even When Icon Works)
**Problem:** Button exists in DOM but not visible on page
**Solution:**
- Use proper color contrast: `bg-blue-600 text-white` (blue background, white text)
- Avoid green backgrounds (`bg-green-600`) as they may have visibility issues
- Add inline styles if needed: `style="display: flex !important; visibility: visible !important; opacity: 1 !important;"`
- Check for CSS conflicts or z-index issues

### 3. Icon Too Small/Large
**Problem:** Icon size is wrong
**Solution:**
- `w-4 h-4` = 16px
- `w-6 h-6` = 24px  
- `w-8 h-8` = 32px

### 4. Icon Color Issues
**Problem:** Icon color doesn't match design
**Solution:**
- Add `text-[color]-[shade]` to parent element
- Use `stroke="currentColor"` in SVG
- Example: `text-white`, `text-blue-600`

## Working Example
```html
<button class="bg-blue-700 text-white rounded-lg w-10 h-10 flex items-center justify-center">
    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" 
         stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
        <path stroke-linecap="round" stroke-linejoin="round" d="..." />
    </svg>
</button>
```

## Checklist Before Adding Icons
- [ ] Use `w-6 h-6` (not `size-6`)
- [ ] Include `stroke="currentColor"`
- [ ] Parent has text color class
- [ ] Use proper button colors (`bg-blue-600 text-white`)
- [ ] Test with hard refresh (Cmd+Shift+R)
- [ ] Restart Spring Boot if needed

## Common Icon Sources
- **Heroicons:** https://heroicons.com/
- **Flaticon:** https://www.flaticon.com/
- **Feather Icons:** https://feathericons.com/ 